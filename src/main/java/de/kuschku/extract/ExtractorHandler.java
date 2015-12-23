package de.kuschku.extract;

import de.kuschku.extract.extractors.*;

import java.util.List;
import java.util.Optional;

public class ExtractorHandler {
    List<VideoExtractor> extractors = Utils.newArrayList(
            new YouTubeExtractor(),
            new GoogleDriveExtractor(),
            new VimeoExtractor(),
            new DailyMotionExtractor()
    );

    public Optional<List<VideoStream>> extract(String url) {
        return extractors.stream()
                .filter(e -> e.canHandleURL(url))
                .findFirst()
                .flatMap(e -> e.extract(e.idFromURL(url)));
    }
}
