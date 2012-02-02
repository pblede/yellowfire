package za.co.yellowfire.setup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.yellowfire.log.LogType;

import java.io.Serializable;

/**
 * @author Mark P Ashworth
 * @version 0.1
 */
public class Version implements Serializable, Comparable<Version> {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(LogType.SETUP.getCategory());

    private int major;
    private int minor;
    private int revision;

    public Version() { }

    public Version (int[] version) {
        if (version == null) { return; }
        if (version.length >= 1) { this.major = version[0]; }
        if (version.length >= 2) { this.minor = version[1]; }
        if (version.length >= 3) { this.revision = version[2]; }
    }

    public Version(int major, int minor, int revision) {
        this.major = major;
        this.minor = minor;
        this.revision = revision;
    }

    public Version(String version) {
        if (version == null) {
            return;
        } else if (version.equals("")) {
            return;
        }

        String[] parts = version.split("\\.");
        int[] info = new int[3];
        for (int i = 0; i < 3 && i < parts.length; i++) {
            try {
                info[i] = Integer.parseInt(parts[i]);
            } catch (NumberFormatException e) {
                LOGGER.warn("Unable to parse version at index {} for version {}", i, version);
            }
        }

        if (info == null) { return; }
        if (info.length >= 1) { this.major = info[0]; }
        if (info.length >= 2) { this.minor = info[1]; }
        if (info.length >= 3) { this.revision = info[2]; }
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getRevision() {
        return revision;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p/>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p/>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p/>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p/>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p/>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     *         is less than, equal to, or greater than the specified object.
     * @throws ClassCastException if the specified object's type prevents it
     *                            from being compared to this object.
     */
    @Override
    public int compareTo(Version o) {
        if (o == null) return 1;

        if (this.major > o.getMajor()) return 1;
        if (this.major < o.getMajor()) return -1;

        if (this.minor > o.getMinor()) return 1;
        if (this.minor < o.getMinor()) return -1;

        if (this.revision > o.getRevision()) return 1;
        if (this.revision < o.getRevision()) return -1;

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Version)) return false;

        Version version = (Version) o;
        return major == version.major && minor == version.minor && revision == version.revision;
    }

    @Override
    public int hashCode() {
        int result = major;
        result = 31 * result + minor;
        result = 31 * result + revision;
        return result;
    }

    public String toString() {
        return new StringBuilder()
                .append(major)
                .append(".")
                .append(minor)
                .append(".")
                .append(revision)
                .toString();
    }
}
