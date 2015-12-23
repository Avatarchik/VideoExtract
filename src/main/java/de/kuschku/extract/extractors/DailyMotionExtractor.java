package de.kuschku.extract.extractors;

import de.kuschku.extract.Utils;
import de.kuschku.extract.VideoStream;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DailyMotionExtractor implements VideoExtractor {
    Pattern pattern = Pattern.compile("^.+dailymotion.com\\/(video|hub)\\/([^_]+)[^#]*(#video=([^_&]+))?");

    @Override
    public Optional<List<VideoStream>> extract(String videoID) {
        return Utils.getPageFromURL(String.format("http://www.dailymotion.com/video/%s", videoID))
                .flatMap(DailyMotionExtractor::getPlayerConfig)
                .map(Utils::stringToJson)
                .map(playerconfig -> playerconfig.getJSONObject("metadata").getJSONObject("qualities"))
                .map(DailyMotionExtractor::getFormatStreams)
                .map(Utils::sortStreams);
    }

    @Override
    public boolean canHandleURL(String url) {
        Matcher matcher = pattern.matcher(url);
        return (matcher.find() && matcher.matches());
    }

    @Override
    public String idFromURL(String url) {
        Matcher matcher = pattern.matcher(url);
        matcher.find();
        return matcher.group(2);
    }

    private static List<VideoStream> getFormatStreams(JSONObject jsonObject) {
        List<VideoStream> list = new ArrayList<>(jsonObject.length());
        for (String key : jsonObject.keySet()) {
            list.add(new VideoStream(
                    jsonObject.getJSONArray(key).getJSONObject(0).getString("url"),
                    jsonObject.getJSONArray(key).getJSONObject(0).getString("type"),
                    VideoStream.Quality.fromHeight(key)
            ));
        }
        return list;
    }

    private static Optional<String> getPlayerConfig(String page) {
        return Utils.extract(page, "buildPlayer({", "});");
    }
}
