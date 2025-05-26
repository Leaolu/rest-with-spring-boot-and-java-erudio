package com.EACH.Security.Util;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserUtil, Long>{

	@Query("SELECT u FROM UserUtil u WHERE u.userName =:userName")
	UserUtil findByName(@Param("userName") String userName);
	
	@Query("SELECT COUNT(u) > 0 FROM UserUtil u WHERE u.userName = :userName")
	Boolean UserNameExists(@Param("userName") String username);

}
