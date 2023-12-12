// PasswordChangeModal.tsx
import React, { useState } from "react";
import styled from "styled-components";

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

  const handleUpdatePassword = () => {
    // 실제로 서버로 비밀번호 변경 요청을 보내는 코드 추가
    // 간단한 예시로 성공/실패 여부를 확인하고 있습니다.
    if (currentPassword !== "password1") {
      setPasswordChangeError("기존 비밀번호와 일치하지 않습니다.");
      return;
    } else if (newPassword !== confirmPassword) {
      setPasswordChangeError("비밀번호를 재확인해주세요.");
      return;
    } else if (
      currentPassword === "password1" &&
      newPassword === confirmPassword
    ) {
      setPasswordChangeError("");
      onClose(); // 모달 닫기
      // 서버로 실제 비밀번호 변경 요청을 보내는 코드를 추가
    } else {
      setPasswordChangeError(
        "비밀번호 변경에 실패했습니다. 다시 시도해주세요.",
      );
    }
  };

  // const handleUpdatePassword = async () => {
  //   try {
  //     // 사용자 정보를 가져오는 API 호출
  //     const response = await axios.get("/api/user");
  //     const actualPassword = response.data.password;

  //     if (currentPassword !== actualPassword) {
  //       setPasswordChangeError("기존 비밀번호와 일치하지 않습니다.");
  //       return;
  //     }
  //     if (newPassword !== confirmPassword) {
  //       setPasswordChangeError("비밀번호를 재확인해주세요.");
  //       return;
  //     }
  //
  //     await axios.post("/api/updatePassword", {
  //       newPassword: newPassword,
  //     });

  //     setPasswordChangeError("");
  //     onClose();
  //   } catch (error) {
  //     console.error("Error updating password:", error);
  //     setPasswordChangeError("비밀번호 변경에 실패했습니다. 다시 시도해주세요.");
  //   }
  // };
  return (
    <ModalOverlay>
      <ModalContent>
        <h2>비밀번호 변경</h2>
        <label>
          기존 비밀번호:{" "}
          <input
            type="password"
            value={currentPassword}
            onChange={(e) => setCurrentPassword(e.target.value)}
          />
        </label>
        <label>
          새로운 비밀번호:{" "}
          <input
            type="password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
          />
        </label>
        <label>
          비밀번호 확인:{" "}
          <input
            type="password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
          />
        </label>
        <button onClick={handleUpdatePassword}>확인</button>
        <button onClick={onClose}>취소</button>
        {passwordChangeError && (
          <p style={{ color: "red" }}>{passwordChangeError}</p>
        )}
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
  border-radius: 5px;
  width: 300px;
  text-align: center;
`;
