package de.kuschku.extract.extractors;

import de.kuschku.extract.VideoStream;

import java.util.List;
import java.util.Optional;

public interface VideoExtractor {
    Optional<List<VideoStream>> extract(String videoID);
    boolean canHandleURL(String url);
    String idFromURL(String url);
}
