import React, { useState, ChangeEvent } from "react";
import axios from "axios";
import { StyledContainer, StyledFormItem, Button } from "./SignUpStyled.tsx";
import * as Regex from "../../common/Regex.ts";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../../axios.tsx";

interface SignUpProps {}

const TEST = "EMAIL_ALREADY_EXIST_EXCEPTION";
//이메일 사용자가 있다고 암시하는 변수명

const SignUp: React.FC<SignUpProps> = () => {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [repassword, setRepassword] = useState<string>("");
  const [name, setName] = useState<string>("");
  const [sexType, setSexType] = useState<string>("");
  const [isIdAvailable, setIsIdAvailable] = useState<boolean | null>(null);
  const [isEmailFormatValid, setIsEmailFormatValid] = useState<boolean | null>(
    null,
  );
  const [isRepasswordValid, setIsRepasswordValid] = useState<boolean>(true);
  const [signupMessage, setSignupMessage] = useState<string | null>(null);
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const navigate = useNavigate();

  const isEmailValid = (email: string): boolean =>
    Regex.emailRegex.test(email.trim());

  const handleSignUp = () => {
    if (
      [email, password, repassword, name].some((value) => value.trim() === "")
    ) {
      setSignupMessage("입력되지 않은 항목이 있습니다.");
      return;
    }
    if (password !== repassword) {
      setSignupMessage("비밀번호가 일치하지 않습니다.");
      setIsRepasswordValid(false);
      return;
    }
    if (password.length < 8) {
      setSignupMessage("비밀번호는 8자리 이상이어야 합니다.");
      return;
    }
    if (!sexType) {
      setSignupMessage("성별을 선택하세요.");
      return;
    }
    if (isIdAvailable === null) {
      setSignupMessage("아이디 중복 확인이 필요합니다.");
      return;
    } else if (!isIdAvailable) {
      setSignupMessage("이미 가입된 이메일입니다.");
      return;
    }
    axiosInstance
      .post(
        "/sign-up",
        {
          email: email,
          password: password,
          name: name,
          repassword: repassword,
          sexType: sexType,
        },
        {
          withCredentials: true,
        },
      )
      .then((res) => {
        console.log(res.data);
        setSignupMessage("회원가입 성공");
        openModal();
      })
      .catch((error) => {
        if (error.response && error.response.data.errorCode === TEST) {
          setSignupMessage("이미 가입된 이메일입니다.");
        } else {
          console.error("회원가입 실패:", error);
          if (error.response) {
            // 서버 응답이 있을 경우
            console.error("서버 응답 데이터:", error.response.data);
            console.error("서버 응답 상태 코드:", error.response.status);
            console.error("서버 응답 헤더:", error.response.headers);
          } else if (error.request) {
            // 요청이 전송되었지만 응답을 받지 못한 경우
            console.error("서버 응답이 없습니다.");
            console.error("요청 데이터:", error.request);
          } else {
            // 오류가 발생한 경우
            console.error("에러 메세지:", error.message);
          }

          setSignupMessage("회원가입 실패");
        }
      });
  };

  const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    switch (name) {
      case "email":
        setEmail(value);
        setIsIdAvailable(null);
        setIsEmailFormatValid(isEmailValid(value));
        break;
      case "password":
        setPassword(value);
        setIsRepasswordValid(true);
        break;
      case "repassword":
        setRepassword(value);
        setIsRepasswordValid(true);
        break;
      case "name":
        setName(value);
        break;
      default:
        break;
    }
  };

  const handleCheckIdAvailability = () => {
    // if (isEmailFormatValid === null || !isEmailFormatValid) {
    //   return;
    // }
    axiosInstance
      .post(`/sign-up/email-check`, { email })
      .then((response) => {
        const isEmailAvailable = response.data.errorCode;
        console.log(isEmailAvailable);
        if (isEmailAvailable === TEST) {
          console.log("중복된 이메일입니다.");
          setIsIdAvailable(false);
        } else {
          setIsIdAvailable(true);
          console.log("사용가능한 이메일입니다.");
        }
      })
      .catch((error) => {
        console.error("이메일 중복 확인 실패:", error);
        if (error.response && error.response.data.errorCode === TEST) {
          // 중복된 이메일 에러 처리
          setIsIdAvailable(false);
          console.log("중복된 이메일입니다.");
        } else if (error.response) {
          // 서버 응답이 있을 경우
          console.error("서버 응답 데이터:", error.response.data);
          console.error("서버 응답 상태 코드:", error.response.status);
          console.error("서버 응답 헤더:", error.response.headers);
        } else if (error.request) {
          // 요청이 전송되었지만 응답을 받지 못한 경우
          console.error("서버 응답이 없습니다.");
          console.error("요청 데이터:", error.request);
        } else {
          // 오류가 발생한 경우
          console.error("에러 메세지:", error.message);
        }
      });
  };
  const openModal = () => {
    setIsModalOpen(true);
  };
  const handleModalConfirm = () => {
    setIsModalOpen(false);
    navigate("/signin");
  };

  return (
    <StyledContainer>
      <h2>회원가입</h2>
      <br />
      <StyledFormItem>
        <input
          type="email"
          id="email"
          name="email"
          value={email}
          onChange={handleInputChange}
          placeholder="이메일"
        />
        <button onClick={handleCheckIdAvailability}>중복 확인</button>
      </StyledFormItem>
      {isIdAvailable !== null && (
        <span style={{ color: isIdAvailable ? "green" : "red" }}>
          {isIdAvailable
            ? "사용 가능한 아이디입니다."
            : "이미 사용 중인 아이디입니다."}
        </span>
      )}
      {isEmailFormatValid !== null && !isEmailFormatValid && (
        <span style={{ color: "red" }}>{"올바른 이메일 형식이 아닙니다."}</span>
      )}
      <br />

      <StyledFormItem>
        <input
          type="password"
          id="password"
          name="password"
          value={password}
          onChange={handleInputChange}
          placeholder="비밀번호 (8자 이상)"
        />
      </StyledFormItem>
      {password.length < 8 && password.length > 0 && (
        <span style={{ color: "red" }}>
          비밀번호는 8자리 이상이어야 합니다.
        </span>
      )}
      <br />

      <StyledFormItem>
        <input
          type="password"
          id="repassword"
          name="repassword"
          placeholder="비밀번호 확인"
          value={repassword}
          onChange={handleInputChange}
          onBlur={() => {
            if (repassword && repassword !== password) {
              setIsRepasswordValid(false);
            } else {
              setIsRepasswordValid(true);
            }
          }}
        />
      </StyledFormItem>
      {isRepasswordValid !== null && (
        <span style={{ color: isRepasswordValid ? "green" : "red" }}>
          {!isRepasswordValid && "비밀번호가 일치하지 않습니다."}
        </span>
      )}
      <br />

      <StyledFormItem>
        <input
          type="text"
          id="name"
          name="name"
          value={name}
          onChange={handleInputChange}
          placeholder="이름"
        />
      </StyledFormItem>
      <br />
      <StyledFormItem>
        <input
          type="radio"
          id="male"
          name="sexType"
          value="MALE"
          checked={sexType === "MALE"}
          onChange={() => setSexType("MALE")}
        />
        <label htmlFor="male">남성</label>

        <input
          type="radio"
          id="female"
          name="sexType"
          value="FEMALE"
          checked={sexType === "FEMALE"}
          onChange={() => setSexType("FEMALE")}
        />
        <label htmlFor="female">여성</label>
      </StyledFormItem>
      <br />

      <StyledFormItem>
        <button onClick={handleSignUp}>회원 가입</button>
      </StyledFormItem>

      {signupMessage && <p style={{ color: "red" }}>{signupMessage}</p>}
      {isModalOpen && (
        <div className="modal">
          <p>아이디로 이메일 인증을 보냈습니다. 확인해주세요.</p>
          <Button onClick={handleModalConfirm}>확인</Button>
        </div>
      )}
    </StyledContainer>
  );
};

export default SignUp;
