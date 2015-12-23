package de.kuschku.extract;

import java.util.List;
import java.util.Map;

public class GoogleUtils {
    public static VideoStream getStreamConfigGoogle(String config) {
        Map<String, String> map = Utils.parse(config);
        return new VideoStream(
                map.get("url"),
                Integer.valueOf(map.get("itag")),
                map.get("type")
        );
    }

    public static String getFormatStreamStringFromMap(Map<String, String> playerConfig) {
        return playerConfig.get("url_encoded_fmt_stream_map");
    }

    public static String[] getFormatStreams(String formatString) {
        return formatString.split(",");
    }

    public static List<VideoStream> getStreams(List<String> strings) {
        return Utils.getStreams(strings, GoogleUtils::getStreamConfigGoogle);
    }
}
