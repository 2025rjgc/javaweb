package com.example.demo.service;

import com.example.demo.entity.circle;

import java.util.List;

public interface CircleService {
    List<circle> findAll();

    circle findById(Integer circleId);

    boolean addCircle(circle circle);

    boolean updateCircle(circle circle);

    boolean deleteCircle(Integer circleId);
}