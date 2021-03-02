package siri.xlite.common;


public class OSMUtils {

    public static final int ZOOM = 14;
    public static final int UPPER_LEFT = 0;
    public static final int BOTTOM_RIGHT = 1;
    public static final int X = 0;
    public static final int Y = 1;

    public static double[][] location(final int x, final int y) {
        return location(x, y, ZOOM);
    }

    public static double[][] location(final int x, final int y, final int z) {
        double[] upperLeft = fromTile(x, y, z);
        double[] bottomRight = fromTile(x + 1, y + 1, z);
        return new double[][]{upperLeft, bottomRight};

    }

    public static double[] fromTile(final int x, final int y, final int zoom) {
        double n = (1 << zoom);
        double lon = (double) x / n * 360d - 180d;
        double lat = Math.atan(Math.sinh(Math.PI * (1d - 2d * (double) y / n))) * 180d / Math.PI;
        return new double[]{lon, lat};
    }

    public static int[] toTile(final double lon, final double lat, final int zoom) {
        double n = (1 << zoom);
        int x = (int) (n * ((lon + 180d) / 360d));
        double lat_rad = Math.toRadians(lat);
        int y = (int) (n * (1 - (Math.log(Math.tan(lat_rad) + 1d / Math.cos(lat_rad)) / Math.PI)) / 2d);
        return new int[]{x, y};
    }

}
