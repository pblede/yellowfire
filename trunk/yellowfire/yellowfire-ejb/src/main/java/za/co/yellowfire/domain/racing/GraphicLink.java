package za.co.yellowfire.domain.racing;

import org.eclipse.persistence.annotations.Converter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Entity(name = "GraphicLink")
@Access(AccessType.FIELD)
@Table(name = "graphic_link", schema = "rce")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "graphic-link", propOrder = { "id", "linkURL", "imageURL",
		"scalePercentage", "alternativeText" })
public class GraphicLink implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "graphic_link_id", table = "graphic_link", nullable = false, insertable = true, updatable = true, length = 19, precision = 0)
	private Long id;

	@XmlAttribute(name = "link-url")
	@Column(name = "graphic_link_url", table = "graphic_link", nullable = true, insertable = true, updatable = true)
	@Converter(name = "graphic_link_url_converter", converterClass = URLConverter.class)
	private String linkURL;

	@XmlAttribute(name = "image-url")
	@Column(name = "graphic_link_image_url", table = "graphic_link", nullable = true, insertable = true, updatable = true)
	// @Converter(name = "graphic_link_image_url_converter", converterClass =
	// URLConverter.class)
	private String imageURL;

	@XmlAttribute(name = "scale")
	@Column(name = "graphic_link_scale_percentage", table = "graphic_link", nullable = true, insertable = true, updatable = true)
	private Integer scalePercentage;

	@XmlAttribute(name = "alt")
	@Column(name = "graphic_link_alt_text", table = "graphic_link", nullable = true, insertable = true, updatable = true)
	private String alternativeText;

	public GraphicLink() {
	}

	public GraphicLink(URL linkURL, URL imageURL, Integer scalePercentage,
			String alternativeText) {
		setLinkURL(linkURL);
		setImageURL(imageURL);
		this.scalePercentage = scalePercentage;
		this.alternativeText = alternativeText;
	}

	public GraphicLink(String linkURL, String imageURL,
			Integer scalePercentage, String alternativeText) {
		this.linkURL = linkURL;
		this.imageURL = imageURL;
		this.scalePercentage = scalePercentage;
		this.alternativeText = alternativeText;
	}

	public Long getId() {
		return id;
	}

	public URL getLinkURL() throws MalformedURLException {
		if (linkURL != null && !linkURL.trim().equals("")) {
			return new URL(linkURL);
		}
		return null;
	}

	public void setLinkURL(URL linkURL) {
		this.linkURL = linkURL.toString();
	}

	public URL getImageURL() throws MalformedURLException {
		if (imageURL != null && !imageURL.trim().equals("")) {
			return new URL(imageURL);
		}
		return null;
	}

	public void setImageURL(URL imageURL) {
		this.imageURL = imageURL.toString();
	}

	public Integer getScalePercentage() {
		return scalePercentage;
	}

	public void setScalePercentage(Integer scalePercentage) {
		this.scalePercentage = scalePercentage;
	}

	public String getAlternativeText() {
		return alternativeText;
	}

	public void setAlternativeText(String alternativeText) {
		this.alternativeText = alternativeText;
	}

	@Override
	public String toString() {
		return "GraphicLink{" + "id=" + id + ", linkURL=" + linkURL
				+ ", imageURL=" + imageURL + ", scalePercentage="
				+ scalePercentage + ", alternativeText=" + alternativeText
				+ '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		GraphicLink that = (GraphicLink) o;

		if (alternativeText != null ? !alternativeText
				.equals(that.alternativeText) : that.alternativeText != null)
			return false;
		if (imageURL != null ? !imageURL.equals(that.imageURL)
				: that.imageURL != null)
			return false;
		if (linkURL != null ? !linkURL.equals(that.linkURL)
				: that.linkURL != null)
			return false;
		if (scalePercentage != null ? !scalePercentage
				.equals(that.scalePercentage) : that.scalePercentage != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = linkURL != null ? linkURL.hashCode() : 0;
		result = 31 * result + (imageURL != null ? imageURL.hashCode() : 0);
		result = 31 * result
				+ (scalePercentage != null ? scalePercentage.hashCode() : 0);
		result = 31 * result
				+ (alternativeText != null ? alternativeText.hashCode() : 0);
		return result;
	}
}
