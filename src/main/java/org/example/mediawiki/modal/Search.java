package org.example.mediawiki.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Search {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;

    @ManyToMany(mappedBy = "searches", cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Pages> pages = new ArrayList<>();

    @OneToMany(mappedBy = "search", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Word> words = new ArrayList<>();

    @Override
    public String toString() {
        return "Search{"
                + "id=" + id
                + ", title='" + title + '\''
                + '}';
    }
}


