// PasswordChangeModal.tsx
import React, { useState } from "react";
import styled from "styled-components";
import axios from "axios";

interface PasswordChangeModalProps {
  onClose: () => void;
}

const PasswordChangeModal: React.FC<PasswordChangeModalProps> = ({
  onClose,
}) => {
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [passwordChangeError, setPasswordChangeError] = useState<string | null>(
    null,
  );
  const token = localStorage.getItem('accessToken');

  // const handleUpdatePassword = () => {
  //   if (currentPassword !== "password1") {
  //     setPasswordChangeError("기존 비밀번호와 일치하지 않습니다.");
  //     return;
  //   } else if (newPassword !== confirmPassword) {
  //     setPasswordChangeError("비밀번호를 재확인해주세요.");
  //     return;
  //   } else if (newPassword.length < 8) {
  //     setPasswordChangeError("비밀번호는 최소 8자 이상이어야 합니다.");
  //     return;
  //   } else if (
  //     currentPassword === "password1" &&
  //     newPassword === confirmPassword
  //   ) {
  //     setPasswordChangeError("");
  //     onClose(); // 모달 닫기
  //     // 서버로 실제 비밀번호 변경 요청을 보내는 코드를 추가
  //   } else {
  //     setPasswordChangeError(
  //       "비밀번호 변경에 실패했습니다. 다시 시도해주세요.",
  //     );
  //   }
  // };

  const handleUpdatePassword = async () => {
    try {
      if (currentPassword !== "password1") {
        setPasswordChangeError("기존 비밀번호와 일치하지 않습니다.");
        return;
      } else if (newPassword !== confirmPassword) {
        setPasswordChangeError("비밀번호를 재확인해주세요.");
        return;
      } else if (newPassword.length < 8) {
        setPasswordChangeError("비밀번호는 최소 8자 이상이어야 합니다.");
        return;
      }
  
      // 서버로 요청을 보내는 코드
      const response = await axios.post(
        'http://localhost:8080/member/password',
        {
          oldpassword: currentPassword,
          newpassword: newPassword,
        },
        {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
        }
      );
  
      //성공 여부만 확인
      if (response.status === 200) {
        setPasswordChangeError('');
        onClose(); 
      } else {
        setPasswordChangeError('비밀번호 변경에 실패했습니다. 다시 시도해주세요.');
      }
    } catch (error) {
      if (axios.isAxiosError(error)) {
        if (error.response?.status === 401) {
          setPasswordChangeError('토큰이 유효하지 않습니다.');
        } else {
          setPasswordChangeError('서버 오류가 발생했습니다.');
        }
      } else {
        setPasswordChangeError('네트워크 오류가 발생했습니다.');
      }
    }
  };

  return (
    <ModalOverlay>
      <ModalContent>
        <ModalHeader>비밀번호 변경</ModalHeader>
        <InputLabel>
          기존 비밀번호:
          <PasswordInput
            type="password"
            value={currentPassword}
            onChange={(e) => setCurrentPassword(e.target.value)}
          />
        </InputLabel>
        <InputLabel>
          새로운 비밀번호:
          <PasswordInput
            type="password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
          />
        </InputLabel>
        <InputLabel>
          비밀번호 확인:
          <PasswordInput
            type="password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
          />
        </InputLabel>
        <ButtonContainer>
          <Button onClick={handleUpdatePassword}>확인</Button>
          <CancelButton onClick={onClose}>취소</CancelButton>
        </ButtonContainer>
        {passwordChangeError && <ErrorText>{passwordChangeError}</ErrorText>}
      </ModalContent>
    </ModalOverlay>
  );
};

export default PasswordChangeModal;

const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5); /* 반투명한 검은 배경 */
  display: flex;
  align-items: center;
  justify-content: center;
`;

const ModalContent = styled.div`
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 300px;
  text-align: center;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
`;

const ModalHeader = styled.h2`
  margin-bottom: 20px;
  color: #333;
`;

const InputLabel = styled.label`
  display: block;
  margin-bottom: 15px;
  text-align: left;
`;

const PasswordInput = styled.input`
  width: 100%;
  padding: 8px;
  margin-top: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: space-around;
  margin-top: 20px;
`;

const Button = styled.button`
  padding: 10px 20px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;

const CancelButton = styled(Button)`
  background-color: #ccc;
`;

const ErrorText = styled.p`
  color: red;
  margin-top: 10px;
`;
