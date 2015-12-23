package de.kuschku.extract.extractors;

import de.kuschku.extract.GoogleUtils;
import de.kuschku.extract.Utils;
import de.kuschku.extract.VideoStream;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouTubeExtractor implements VideoExtractor {
    Pattern pattern = Pattern.compile("(?:https?://)?(?:youtu\\.be/|(?:www\\.)?youtube\\.com/(watch(?:\\.php)?\\?.*v=|v/|embed/))([a-zA-Z0-9\\-_]+)");

    @Override
    public Optional<List<VideoStream>> extract(String videoID) {
        return Utils.getPageFromURL(String.format("https://www.youtube.com/watch?v=%s",videoID))
                .flatMap(YouTubeExtractor::getPlayerConfig)
                .map(Utils::stringToJson)
                .map(YouTubeExtractor::getFormatStreamString)
                .map(GoogleUtils::getFormatStreams)
                .map(Arrays::asList)
                .map(GoogleUtils::getStreams)
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

    private static Optional<String> getPlayerConfig(String page) {
        return Utils.extract(page, ";ytplayer.config = {", "};");
    }

    private static String getFormatStreamString(JSONObject playerConfig) {
        return playerConfig.getJSONObject("args").getString("url_encoded_fmt_stream_map");
    }

}
