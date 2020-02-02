package com.xujiacheng.avmooviewer.utils;


import com.xujiacheng.avmooviewer.itembean.Actor;
import com.xujiacheng.avmooviewer.itembean.Av;
import com.xujiacheng.avmooviewer.itembean.Category;
import com.xujiacheng.avmooviewer.itembean.Info;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Crawler {


    public static ArrayList<Av> getAvList(String html) {
        ArrayList<Av> avArrayList = new ArrayList<>();
        Document document = Jsoup.parse(html);
        //document.querySelector("body > div.container-fluid > div.alert.alert-danger")
        Element warning = document.select("body > div.container-fluid > div.alert.alert-danger").first();
        if (warning != null) {
            return avArrayList;
        } else {
            Elements movieBoxElements = document.select("#waterfall > div > a");

            for (Element movieBoxElement : movieBoxElements) {
                String url = movieBoxElement.attr("href");
                String coverURL = movieBoxElement.select("div.photo-frame > img").attr("src");
                String name = movieBoxElement.select("div.photo-frame > img").attr("title");
                String id = movieBoxElement.select("div.photo-info > span > date:nth-child(2)").text();
                String releaseDate = movieBoxElement.select("div.photo-info > span > date:nth-child(3)").text();
//            Log.d(TAG, String.format("url = %s, name = %s, id = %s, date = %s.", coverURL, name, id, releaseDate));
                Av av = new Av();
                av.url = url;
                av.name = name;
                av.coverURL = coverURL;
                av.id = id;
                av.releaseDate = releaseDate;
                avArrayList.add(av);
            }
            return avArrayList;
        }
    }

    public static Av getAvInformation(String response) {
        Av av = new Av();

        Document document = Jsoup.parse(response);
        //获取标题
        //title document.querySelector("body > div.container > h3")
        av.name = document.select("body > div.container > h3").text();
        //获取封面大图片
        // cover document.querySelector("body > div.container > div.row.movie > div.col-md-9.screencap > a > img")
        av.bigCoverURL = document.select("body > div.container > div.row.movie > div.col-md-9.screencap > a.bigImage > img").attr("src");
        //document.querySelector("body > div.container > div.row.movie > div.col-md-3.info > p:nth-child(1)")
        //获取详细信息
        Elements infoElements = document.select("body > div.container > div.row.movie > div.col-md-3.info > p");
        for (Element infoElement : infoElements) {
            Elements span = infoElement.getElementsByTag("span");
            if (span != null && span.size() != 0) {
                switch (span.first().text()) {
                    case "识别码:":
                        av.id = span.last().text();
                        break;
                    case "发行时间:":
                        av.releaseDate = infoElement.text();
                        break;
                    case "长度:":
                        av.duration = infoElement.text();
                        break;
                    case "导演:":
                        Info info = new Info();
                        info.name = infoElement.getElementsByTag("a").text();
                        info.url = infoElement.getElementsByTag("a").attr("href");
                        av.director = info;
                        break;
                }
            }
        }
        //获取演员信息
//        document.querySelector("#avatar-waterfall > a")
        Elements actorElements = document.select("#avatar-waterfall > a.avatar-box");
        if (actorElements != null && actorElements.size() > 0) {
            ArrayList<Info> actors = new ArrayList<>();
            for (Element actorElement : actorElements) {
                String actorName = actorElement.getElementsByTag("span").text();
                String url = actorElement.attr("href");
                String imageURL = actorElement.select("div.photo-frame > img").attr("src");
                actors.add(new Info(actorName, url, imageURL));
            }
            av.actors = actors;
        }
        //获取预览图片
        // document.querySelector("#sample-waterfall > a:nth-child(1)")
        Elements previewElements = document.select("#sample-waterfall > a.sample-box");
        if (previewElements != null && previewElements.size() > 0) {
            av.previewURL = new ArrayList<>();
            for (Element previewElement : previewElements) {
                String pic = previewElement.attr("href");
                av.previewURL.add(pic);
            }
        }
        return av;
    }

    public static ArrayList<Actor> getActresses(String html) {
        Document document = Jsoup.parse(html);
        // document.querySelector("#waterfall > div:nth-child(1) > a")
        Elements actressesElements = document.select("#waterfall > div > a");
        ArrayList<Actor> actors = new ArrayList<>();
        for (Element actressesElement : actressesElements) {
            String url = actressesElement.attr("href");
            String imagURL = actressesElement.select("div.photo-frame > img").attr("src");
            String name = actressesElement.select("div.photo-info > span").text();
            actors.add(new Actor(url, name, imagURL));
        }
        return actors;
    }



    public static ArrayList<Category> getCategoryList(String html) {
        ArrayList<Category> categories = new ArrayList<>();
        Document document = Jsoup.parse(html);
        //document.querySelector("body > div.container-fluid.pt-10 > div:nth-child(2) > a:nth-child(1)")
        Elements elements = document.select("body > div.container-fluid > div > a");

        for (Element element : elements) {
            String url = element.attr("href");

            String name = element.text();

            categories.add(new Category(name, url));
        }
        return categories;
    }
}
