package de.kuschku.extract;

import de.kuschku.extract.extractors.DailyMotionExtractor;
import de.kuschku.extract.extractors.GoogleDriveExtractor;
import de.kuschku.extract.extractors.VimeoExtractor;
import de.kuschku.extract.extractors.YouTubeExtractor;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VideoExtractorTest {
    @Test
    public void testExtractYouTube() throws Exception {
        Optional<List<VideoStream>> videoStreams = new YouTubeExtractor().extract("YSAqTdc-Y2g");
        assertTrue(videoStreams.isPresent());
        assertTrue(videoStreams.get().size() > 0);
        videoStreams.get().stream().map(stream -> stream.url).map(Utils::exists).forEach(Assert::assertTrue);
        System.out.println(videoStreams);
    }

    @Test
    public void testExtractGoogleDrive() throws Exception {
        Optional<List<VideoStream>> videoStreams = new GoogleDriveExtractor().extract("0B5jYG6FigBRGeExKRF9pMlVyR0E");
        assertTrue(videoStreams.isPresent());
        assertTrue(videoStreams.get().size() > 0);
        videoStreams.get().stream().map(stream -> stream.url).map(Utils::exists).forEach(Assert::assertTrue);
        System.out.println(videoStreams);
    }

    @Test
    public void testExtractVimeo() throws Exception {
        Optional<List<VideoStream>> videoStreams = new VimeoExtractor().extract("123259611");
        assertTrue(videoStreams.isPresent());
        assertTrue(videoStreams.get().size() > 0);
        videoStreams.get().stream().map(stream -> stream.url).map(Utils::exists).forEach(Assert::assertTrue);
        System.out.println(videoStreams);
    }

    @Test
    public void testExtractDailyMotion() throws Exception {
        Optional<List<VideoStream>> videoStreams = new DailyMotionExtractor().extract("x372gue");
        assertTrue(videoStreams.isPresent());
        assertTrue(videoStreams.get().size() > 0);
        videoStreams.get().stream().map(stream -> stream.url).map(Utils::exists).forEach(Assert::assertTrue);
        System.out.println(videoStreams);
    }

    @Test
    public void testExtractYouTubeBlocked() throws Exception {
        assertFalse(new YouTubeExtractor().extract("-mHtizddaUQ").isPresent());
    }

    @Test
    public void testExtractYouTubeNotAvailable() throws Exception {
        assertFalse(new YouTubeExtractor().extract("trololol").isPresent());
    }

    @Test
    public void testExtractGoogleDriveNotAvailable() throws Exception {
        assertFalse(new GoogleDriveExtractor().extract("trololol").isPresent());
    }

    @Test
    public void testExtractVimeoNotAvailable() throws Exception {
        assertFalse(new VimeoExtractor().extract("trololol").isPresent());
    }

    @Test
    public void testExtractDailyMotionNotAvailable() throws Exception {
        assertFalse(new DailyMotionExtractor().extract("trololol").isPresent());
    }
}