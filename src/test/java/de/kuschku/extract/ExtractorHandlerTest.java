package de.kuschku.extract;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExtractorHandlerTest {
    ExtractorHandler handler = new ExtractorHandler();

    private static void checkStreams(Optional<List<VideoStream>> videoStreams) {
        System.out.println(videoStreams);
        assertTrue(videoStreams.isPresent());
        assertTrue(videoStreams.get().size() > 0);
        videoStreams.get().stream().map(stream -> stream.url).map(Utils::exists).forEach(Assert::assertTrue);
    }

    @Test
    public void testExtractYouTube() throws Exception {
        Optional<List<VideoStream>> videoStreams = handler.extract("http://youtube.com/watch?v=YSAqTdc-Y2g");
        checkStreams(videoStreams);
    }

    @Test
    public void testExtractGoogleDrive() throws Exception {
        Optional<List<VideoStream>> videoStreams = handler.extract("https://drive.google.com/file/d/0ByFIZpcDWLXvS0tWT1B2d0lJV0U/view?usp=sharing");
        checkStreams(videoStreams);
    }

    @Test
    public void testExtractVimeo() throws Exception {
        Optional<List<VideoStream>> videoStreams = handler.extract("https://vimeo.com/35270069");
        checkStreams(videoStreams);
    }

    @Test
    public void testExtractDailyMotion() throws Exception {
        Optional<List<VideoStream>> videoStreams = handler.extract("http://www.dailymotion.com/video/x372gue");
        checkStreams(videoStreams);
    }

    @Test
    public void testExtractYouTubeBlocked() throws Exception {
        assertFalse(handler.extract("https://www.youtube.com/watch?v=-mHtizddaUQ&list=PLA8VHLKYzqTvCcIKhsjGS41nG1zBpRZPy").isPresent());
    }

    @Test
    public void testExtractYouTubeNotAvailable() throws Exception {
        assertFalse(handler.extract("https://www.youtube.com/watch?v=trololol").isPresent());
    }

    @Test
    public void testExtractGoogleDriveNotAvailable() throws Exception {
        assertFalse(handler.extract("https://drive.google.com/file/d/trkjziorzh/view?usp=sharing").isPresent());
    }

    @Test
    public void testExtractVimeoNotAvailable() throws Exception {
        assertFalse(handler.extract("https://vimeo.com/666").isPresent());
    }

    @Test
    public void testExtractDailyMotionNotAvailable() throws Exception {
        assertFalse(handler.extract("http://www.dailymotion.com/video/trololol").isPresent());
    }
}