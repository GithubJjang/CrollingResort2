package com.example.demo.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Resort;

public interface UserResortRepository extends JpaRepository<Resort, Integer> {

	Resort findById(int Id);// resort id를 이용해서 조회를 한다.
}
