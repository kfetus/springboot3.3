package demo.sample.vuejs;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import demo.common.vo.UserVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * 아주 기본적인 jwt 셋팅 클래스. bearer 를 Authorization에 셋팅하고 자시고 머 그닥 필수는 아님. 중요한건 키를 가지고 암호화 해서 header 에 셋팅하고 주고받는부분
 * Expire Time을 선언해서 하는 부분. 갱신 하는 부분은 구현 안함
 * 
 * accessToken은 로그인을 유지 하는데 이용하고 refreshTokem은 재발급에 관여한다.
 * 하지만 둘다 같은 방식으로 생성하고 expire Time만 다르게 (refreshToken을 길게~~) 하는것 밖에 없다. accessToken이 
 * io.jsonwebtoken.ExpiredJwtException 이 발생하면 refreshToken을 재전송 받아서 갱신해주는 방식이다. 즉 매번 accessToken을 갱신해버리면 
 * refreshToken을 쓸 일이 없어진다.
 *  
 * 일반적인 설계 공식
 * accessToken Expire Time 설정 : 일반적으로 사이트(또는 OpenApi)에 로그인해서 활동하는 시간(10~30분 적정). refreshToken ExpireTime은 목적에 맞게
 * 해당 시간내 Token 갱신은 없다
 * Expire Time이 지나서 Exception이 나면 클라이언트에 refreshToken을 요청해서 서버에 저장된 값과 동일하면 accessToken 및 refreshToken을 갱신해준다.
 * 
 * @author USER
 *
 */
@Component
public class JwtComponent {

	private final Logger LOGGER = LoggerFactory.getLogger(JwtComponent.class);
	
    @Value("${control.jwt.baseKey}")
    private String JWT_KEY;	
	
	@Value("${control.jwt.baseTime}")
	private long JWT_BASE_TIME ;//300000
	
	@Value("${control.jwt.refreshTime}")
	private long JWT_REFRESH_TIME ;//3600000
	
	//header에서 주고 받을 키값. 아무 이름이나 써도 된다.
	public final String HEADER_KEY = "AccessKeyJwt";

	/**
	 * 
	 * @param userVO  = UserVO
	 * @param subject = "Authorization"
	 * @return
	 */
	public String makeToken(UserVO userVO, String subject ) {

		Map<String, Object> jwtInfo = new HashMap<>();

		jwtInfo.put("id", userVO.getUserId());
		jwtInfo.put("name", userVO.getUserName());
		jwtInfo.put("grade", userVO.getGrade());
		jwtInfo.put("hpno", userVO.getHpNo());

		LOGGER.debug("######secret=" + JWT_KEY);

		// 무작위 키값 만들기. 서버가 여러대면 서버마다 키값이 틀리다. 단독 서버일 경우에도 서버가 Restart 한다면 해당 값이 날라간다.
		// SecretKey key = Jwts.SIG.HS256.key().build();

		//expire Time 30분(1800000)
		return Jwts.builder().claims(jwtInfo).subject(subject).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + JWT_BASE_TIME))
				.signWith(Keys.hmacShaKeyFor(JWT_KEY.getBytes()), Jwts.SIG.HS512).compact();
	}

	
	/**
	 * refresh 토큰은 accessToken과 동일하게 생성해서 클라이언트에게 내려준다. 다만 시간이 다를뿐이다. 
	 * 프로세스 : 1. accessToken으로 접근 2.verifyJWT에서 ExpireException 발생 3. client에게 해당 에러 전송 4. client는 해당 에러시 갱신 프로세스 진행
	 * 5. Token 갱신 url에 sessionStorage에 있는 refreshToken을 담아서 전송. 
	 * 6. 서버는 최초 로그인시 로그인시의 특정 정보(ID 같은거)를 PK로 refreshToken은 따로 (DB, Redis, 등등)에 저장하고 있다가 
	 * client에서 전송받은 refreshToken을 decode해서 claims에 있는 키값으로 조회해서 값이 있고, 서로 같으면 accessToken을 새로 발급해서 전송
	 * 7. client는 받은 값으로 accessToken을 저장. 이때 refreshToken을 갱신하지 않는 이유는 expire Time을 충분히 길게(예를 들면 3시간) 잡았다는 가정기준이므로 
	 * @param userVO  = UserVO
	 * @param subject = "Authorization"
	 * @return
	 */
	public String makeRefreshToken(UserVO userVO, String subject ) {

		Map<String, Object> jwtInfo = new HashMap<>();

		jwtInfo.put("id", userVO.getUserId());
		jwtInfo.put("name", userVO.getUserName());
		jwtInfo.put("grade", userVO.getGrade());
		jwtInfo.put("hpno", userVO.getHpNo());

		LOGGER.debug("######secret=" + JWT_KEY);

		return Jwts.builder().claims(jwtInfo).subject(subject).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + JWT_REFRESH_TIME))
				.signWith(Keys.hmacShaKeyFor(JWT_KEY.getBytes()), Jwts.SIG.HS512).compact();
	}

	
	public boolean verifyJWT(String token) {
		boolean state = true;
		try {
			LOGGER.debug("###### verifyJWT token=" + token);
			Jws<Claims> claimJwt = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(JWT_KEY.getBytes())).build()
					.parseSignedClaims(token);
			Claims claims = claimJwt.getPayload();
			LOGGER.debug("###### verifyJWT parsing claims=" + claims);
			if( !StringUtils.hasText((String)claims.get("id"))) {
				state = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			state =false;
//			throw e;
		}
		return state;
	}

	public Map<String, Object> decoderClaim(String token){
		LOGGER.debug("###### decoderClaim token=" + token);
		
		Map<String, Object> decodeClaim = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(JWT_KEY.getBytes()))
	            .build().parseSignedClaims(token).getPayload();
	    return decodeClaim;
	}
	
	
	public static void main(String[] args) {
		System.out.println("###### START ######");

		JwtComponent jwt = new JwtComponent();
		
		jwt.JWT_KEY="TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT";
//		jwt.JWT_KEY="255bytemorekeylength-so-no-reason-data-domain-version-target98761&&^^%**@";
		UserVO vo = new UserVO();
		vo.setUserId("TESXT");
		vo.setUserName("아무개");
		vo.setGrade("M");
		vo.setHpNo("01012345678");

		String token = jwt.makeToken(vo, jwt.HEADER_KEY);
		System.out.println("###### jwt token="+token);
		System.out.println("###### verifyJWT(token)="+jwt.verifyJWT(token));
		System.out.println("###### parsing(token)="+jwt.decoderClaim(token));
		
		
//		System.out.println("###### verifyJWT(token)="+jwt.verifyJWT("eyJhbGciOiJIUzUxMiJ9.eyJncmFkZSI6Ik0iLCJuYW1lIjoi7Jik7Yag7KSR6rOg7LCoIiwiaHBubyI6IjAxMDU1NTU0NDQ0IiwiaWQiOiJhdXRvIiwic3ViIjoiQXV0aG9yaXphdGlvbiIsImlhdCI6MTcxNDM4MDkxMywiZXhwIjoxNzE0MzgyNzEzfQ.qTROxPZ1hVW9UNVo1JOdp2FaJjb0SYwRRoxrKqYPkVtQK8r6p3HWr0DfDyLMZy0sMXn3Us8oKWu6SZj2tHr-MQ"));
		
		
		System.out.println("###### END ######");
	}

}
