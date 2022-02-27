package com.example.noteapi.data;

import java.time.Instant;
import java.util.List;

public class User {
    public String id;
    public String name;
    public Instant joinDate;
    public List<String> noteLabels;
}
