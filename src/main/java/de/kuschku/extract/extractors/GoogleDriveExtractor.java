package de.kuschku.extract.extractors;

import de.kuschku.extract.GoogleUtils;
import de.kuschku.extract.Utils;
import de.kuschku.extract.VideoStream;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoogleDriveExtractor implements VideoExtractor {
    Pattern pattern = Pattern.compile("https?://drive\\.google\\.com/file/d/(.*?)/.*");

    @Override
    public Optional<List<VideoStream>> extract(String videoID) {
        return Utils.getPageFromURL(String.format("https://docs.google.com/file/d/%s/get_video_info?sle=true", videoID))
                .map(Utils::parse)
                .map(GoogleUtils::getFormatStreamStringFromMap)
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
        return matcher.group(1);
    }
}
