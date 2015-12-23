package de.kuschku.extract.extractors;

import de.kuschku.extract.Utils;
import de.kuschku.extract.VideoStream;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VimeoExtractor implements VideoExtractor {
    Pattern pattern = Pattern.compile("https?://(?:www\\.|player\\.)?vimeo.com/(?:channels/(?:\\w+/)?|groups/([^/]*)/videos/|album/(\\d+)/video/|video/|)(\\d+)(?:$|/|\\?)");

    @Override
    public Optional<List<VideoStream>> extract(String videoID) {
        return Utils.getPageFromURL(String.format("https://player.vimeo.com/video/%s/config", videoID))
                .map(Utils::stringToJson)
                .map(VimeoExtractor::getStreamList)
                .map(Utils::jsonArrayToObjectList)
                .map(VimeoExtractor::toStreams)
                .map(Utils::sortStreams);
    }

    private static List<VideoStream> toStreams(List<JSONObject> jsonObjects) {
        return Utils.getStreams(jsonObjects, VimeoExtractor::getStreamConfig);
    }

    private static JSONArray getStreamList(JSONObject jsonObject) {
        return jsonObject.getJSONObject("request").getJSONObject("files").getJSONArray("progressive");
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
        return matcher.group(3);
    }

    private static VideoStream getStreamConfig(JSONObject streamconfig) {
        return new VideoStream(
                streamconfig.getString("url"),
                streamconfig.getString("mime"),
                VideoStream.Quality.fromHeight(streamconfig.getInt("height"))
        );
    }
}
