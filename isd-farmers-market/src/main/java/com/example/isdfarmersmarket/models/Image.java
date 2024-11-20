package com.example.isdfarmersmarket.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name="images")
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    private Long id;
    private String name;
    @Column(name = "original_file_name")
    private String originalFileName;
    private Long size;
    @Column(name = "content_type")

    private String contentType;
    @Lob
    @Column(name = "bytes", columnDefinition = "BIGINT")
    private byte[] bytes;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        Image image = (Image) o;
        return Objects.equals(getId(), image.getId());
    }
    private String base64Image;
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}
