package com.example.GuessTheNumber.services;
import com.example.GuessTheNumber.models.Dummy;

import java.util.List;

public interface DummyService {

    Dummy getDummy(Long id);
    List<Dummy> getDummyList();
    Dummy createDummy(Dummy dummy);
    Dummy updateDummy(Dummy dummy);
    void deleteDummy(Dummy dummy);
}
