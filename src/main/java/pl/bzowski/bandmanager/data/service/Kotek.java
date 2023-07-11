package pl.bzowski.bandmanager.data.service;

import org.springframework.data.domain.Page;
import pl.bzowski.bandmanager.data.entity.Musician;

import java.util.stream.Stream;

public class Kotek {
    private Page<Musician> all;

    public Kotek() {

    }

    public Kotek(Page<Musician> all) {
        this.all = all;
    }

    public Page<Musician> getPage() {
        return all;
    }
}
