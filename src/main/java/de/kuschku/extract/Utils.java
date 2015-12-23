package de.kuschku.extract;

import de.kuschku.extract.extractors.*;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Utils {
    public static Map<String, String> parse(final String query) {
        List<String> strings = Arrays.asList(query.split("&"));
        List<String[]> splits = strings.stream().map(p -> p.split("=")).collect(Collectors.toList());
        Map<String, String> map = new HashMap<>(splits.size());
        for (String[] s : splits) {
            map.put(decode(index(s, 0)), decode(index(s, 1)));
        }
        return map;
    }

    public static <T> T index(final T[] array, final int index) {
        return index >= array.length ? null : array[index];
    }

    public static String decode(final String encoded) {
        try {
            return encoded == null ? null : URLDecoder.decode(encoded, "UTF-8");
        } catch(final UnsupportedEncodingException e) {
            throw new RuntimeException("Impossible: UTF-8 is a required encoding", e);
        }
    }

    public static List<JSONObject> jsonArrayToObjectList(JSONArray arr) {
        List<JSONObject> l = new ArrayList<>(arr.length());
        for (int i = 0; i < arr.length(); i++) {
            l.add(arr.getJSONObject(i));
        }
        return l;
    }

    public static boolean exists(String URLName){
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
            con.setRequestMethod("HEAD");
            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_MOVED_TEMP
                    || status == HttpURLConnection.HTTP_MOVED_PERM
                    || status == HttpURLConnection.HTTP_SEE_OTHER) {
                return exists(con.getHeaderField("Location"));
            } else {
                return (status == HttpURLConnection.HTTP_OK);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Optional<String> extract(String content, String beginToken, String endToken)  {
        if (!content.contains(beginToken) || !content.contains(endToken)) return Optional.empty();

        int begin = content.indexOf(beginToken);
        int end = content.indexOf(endToken, begin);
        return Optional.of(content.substring(begin+beginToken.length()-1, end+1));
    }

    public static List<VideoStream> sortStreams(List<VideoStream> streams) {
        Collections.sort(streams, (x, y) -> x.quality.ordinal() - y.quality.ordinal());
        return streams;
    }

    public static Optional<String> getPageFromURL(String url) {
        try (InputStream in = new URL(url).openStream()) {
            return Optional.of(IOUtils.toString(in));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public static JSONObject stringToJson(String config) {
        return new JSONObject(config);
    }

    public static <T> List<VideoStream> getStreams(List<T> formatStreamStrings, Function<T, VideoStream> function) {
        return formatStreamStrings.stream().map(function).collect(Collectors.toList());
    }

    public static <T> List<T> newArrayList(T... elem) {
        return Arrays.asList(elem);
    }
}
