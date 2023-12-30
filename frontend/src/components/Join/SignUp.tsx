import React, { useState, ChangeEvent } from "react";
import {
  StyledContainer,
  StyledFormItem,
  GreenText,
  StyledText,
  StyledSignUp,
  RedText,
} from "./SignUpStyled.tsx";
import * as Regex from "../../common/Regex.ts";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../../axios.tsx";

interface SignUpProps {}

const TEST = "EMAIL_ALREADY_EXIST_EXCEPTION";

const SignUp: React.FC<SignUpProps> = () => {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [rePassword, setRePassword] = useState<string>("");
  const [name, setName] = useState<string>("");
  const [sexType, setSexType] = useState<string>("");
  const [isIdAvailable, setIsIdAvailable] = useState<boolean | null>(null);
  const [isEmailFormatValid, setIsEmailFormatValid] = useState<boolean | null>(
    null,
  );
  const [isRePasswordValid, setIsRePasswordValid] = useState<boolean>(true);
  const [signUpMessage, setSignUpMessage] = useState<string | null>(null);
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const navigate = useNavigate();

  const isEmailValid = (email: string): boolean =>
    Regex.emailRegex.test(email.trim());

  const handleSignUp = () => {
    if (
      [email, password, rePassword, name].some((value) => value.trim() === "")
    ) {
      setSignUpMessage("입력되지 않은 항목이 있습니다.");
      return;
    }
    if (password !== rePassword) {
      setSignUpMessage("비밀번호가 일치하지 않습니다.");
      setIsRePasswordValid(false);
      return;
    }
    if (password.length < 8) {
      setSignUpMessage("비밀번호는 8자리 이상이어야 합니다.");
      return;
    }
    if (!sexType) {
      setSignUpMessage("성별을 선택하세요.");
      return;
    }
    if (isIdAvailable === null) {
      setSignUpMessage("아이디 중복 확인이 필요합니다.");
      return;
    } else if (!isIdAvailable) {
      setSignUpMessage("이미 가입된 이메일입니다.");
      return;
    }
    axiosInstance
      .post(
        "/sign-up",
        {
          email: email,
          password: password,
          name: name,
          rePassword: rePassword,
          sexType: sexType,
        },
        {
          withCredentials: true,
        },
      )
      .then(() => {
        setSignUpMessage("회원가입 성공");
        openModal();
      })
      .catch((error) => {
        if (error.response && error.response.data.errorCode === TEST) {
          setSignUpMessage("이미 가입된 이메일입니다.");
        } else {
          console.error("회원가입 실패:", error);
          setSignUpMessage("회원가입 실패");
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
        setIsRePasswordValid(true);
        break;
      case "rePassword":
        setRePassword(value);
        setIsRePasswordValid(true);
        break;
      case "name":
        setName(value);
        break;
      default:
        break;
    }
  };

  const handleCheckIdAvailability = () => {
    axiosInstance
      .post(`/sign-up/email-check`, { email })
      .then((response) => {
        const isEmailAvailable = response.data.errorCode;
        if (isEmailAvailable === TEST) {
          setIsIdAvailable(false);
        } else {
          setIsIdAvailable(true);
        }
      })
      .catch((error) => {
        console.error("이메일 중복 확인 실패:", error);
        if (error.response && error.response.data.errorCode === TEST) {
          setIsIdAvailable(false);
        }
      });
  };
  const openModal = () => {
    setIsModalOpen(true);
  };
  const handleModalConfirm = () => {
    setIsModalOpen(false);
    navigate("/signIn");
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
        <>
          {isIdAvailable ? (
            <GreenText>사용 가능한 아이디입니다.</GreenText>
          ) : (
            <RedText>이미 사용 중인 아이디입니다.</RedText>
          )}
        </>
      )}
      {isEmailFormatValid !== null && !isEmailFormatValid && (
        <RedText>올바른 이메일 형식이 아닙니다.</RedText>
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
        <RedText>비밀번호는 8자리 이상이어야 합니다.</RedText>
      )}
      <br />

      <StyledFormItem>
        <input
          type="password"
          id="rePassword"
          name="rePassword"
          placeholder="비밀번호 확인"
          value={rePassword}
          onChange={handleInputChange}
          onBlur={() => {
            if (rePassword && rePassword !== password) {
              setIsRePasswordValid(false);
            } else {
              setIsRePasswordValid(true);
            }
          }}
        />
      </StyledFormItem>
      {isRePasswordValid !== null && (
        <RedText>
          {!isRePasswordValid && "비밀번호가 일치하지 않습니다."}
        </RedText>
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

      {signUpMessage && <StyledSignUp>{signUpMessage}</StyledSignUp>}
      {isModalOpen && (
        <div>
          <StyledText>아이디로 이메일 인증을 보냈습니다. </StyledText>
          <button onClick={handleModalConfirm}>확인</button>
        </div>
      )}
    </StyledContainer>
  );
};

export default SignUp;
