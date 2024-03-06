package org.example.mediawiki.modal;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Pages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private long pageId;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "pages_search",
            joinColumns = @JoinColumn(name = "page_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "search_id", referencedColumnName = "id"))
    private List<Search> searchSet = new ArrayList<>();
}
