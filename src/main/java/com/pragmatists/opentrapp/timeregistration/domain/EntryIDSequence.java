package com.pragmatists.opentrapp.timeregistration.domain;


import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry.EntryID;

public interface EntryIDSequence {

    public EntryID nextID();

}
