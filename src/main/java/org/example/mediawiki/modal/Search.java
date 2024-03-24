package org.example.mediawiki.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.Entity;
import javax.persistence.Id;
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

    @ManyToMany(mappedBy = "searches", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Pages> pages = new ArrayList<>();

    @OneToMany(mappedBy = "search", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Word> words = new ArrayList<>();

    @Override
    public String toString() {
        return "Search{"
                + "id=" + id
                + ", title='" + title + '\''
                + '}';
    }
}


