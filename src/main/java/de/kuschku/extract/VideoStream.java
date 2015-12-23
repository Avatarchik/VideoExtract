package de.kuschku.extract;

public class VideoStream {
    public final String url;
    public final String type;
    public final Quality quality;

    public VideoStream(String url, int itag, String type) {
        this.url = url;
        this.type = getMIME(type);
        this.quality = Quality.fromItag(itag);
    }

    private String getMIME(String type) {
        if (type.contains(";")) {
            return type.substring(0, type.indexOf(";"));
        } else {
            return type;
        }
    }

    public VideoStream(String url, String type, Quality quality) {
        this.url = url;
        this.type = getMIME(type);
        this.quality = quality;
    }

    @Override
    public String toString() {
        return "VideoStream{" +
                "url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", quality=" + quality +
                '}';
    }

    public enum Quality {
        HD1080,
        HD720,
        LARGE,
        MEDIUM,
        SMALL,
        UNKNOWN;

        public static Quality fromString(String str) {
            switch (str) {
                case "hd1080":
                    return HD1080;
                case "hd720":
                    return HD720;
                case "large":
                    return LARGE;
                case "medium":
                    return MEDIUM;
                case "small":
                    return SMALL;
                default:
                    return UNKNOWN;
            }
        }

        public static Quality fromItag(int itag) {
            switch (itag) {
                case 37:
                case 46:
                    return HD1080;
                case 22:
                case 45:
                    return HD720;
                case 59:
                case 44:
                    return LARGE;
                case 35:
                case 18:
                    return MEDIUM;
                case 43:
                case 34:
                    return SMALL;
                default:
                    return UNKNOWN;
            }
        }

        public static Quality fromHeight(int height) {
            if (height >= 1080) return HD1080;
            if (height >=  720) return HD720;
            if (height >=  480) return LARGE;
            if (height >=  360) return MEDIUM;
            if (height >=  240) return SMALL;
            return UNKNOWN;
        }

        public static Quality fromHeight(String key) {
            try {
                return fromHeight(Integer.valueOf(key));
            } catch (NumberFormatException|NullPointerException e) {
                return UNKNOWN;
            }
        }
    }
}
