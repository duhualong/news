package dhl.com.project.domain;


import java.util.ArrayList;

/**
 * 网络分类信息的封装
 * 字段名字必须和服务器返回的名字一致，方便Gson解析
 */
public class NewsData {
    public  int retcode;
    public ArrayList<NewsMenuData> data;
    //侧边栏数据对象
   public class  NewsMenuData{
        public  String id;
        public String title;
        public int type;
        public String url;
        public ArrayList<NewsTabData> children;

        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "children=" + children +
                    ", title='" + title + '\'' +
                    '}';
        }
    }
    //新闻页面下11个子页签的数据对象
   public class NewsTabData{
        public  String id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NewsData{" +
                "data=" + data +
                '}';
    }
}
