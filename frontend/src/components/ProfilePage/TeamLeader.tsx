import React, { useState, ChangeEvent, useEffect } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import profileImg from "../../assets/profileImg.png";
import StyledModal from "../Modal";
import axiosInstance from "../../axios";
import { Team, TeamParticipant } from "../../interface/interface.ts";
import { accessTokenState } from "../../state/authState";
import { useRecoilValue } from "recoil";
import linkImg from "../../assets/linkImg.png";
import {
  TeamLeaderContainer,
  TeamImageContainer,
  TeamInfoContainer,
  StyledButton,
  StyledInput,
  TeamMembersContainer,
  ConfirmationModal,
} from "./TeamLeaderStyled";

export default function TeamLeader() {
  const { state } = useLocation();
  const { teamId } = useParams();
  const accessToken = useRecoilValue(accessTokenState);
  const teamFromPreviousPage = state?.team || null;
  const [team, setTeam] = useState<Team | null>(teamFromPreviousPage);
  const [searchTeam, setSearchTeam] = useState<string>("");
  const [showConfirmation, setShowConfirmation] = useState(false);
  const [memberIndexToRemove, setMemberIndexToRemove] = useState<number | null>(
    null,
  );
  const [isDeleteConfirmationOpen, setDeleteConfirmationOpen] = useState(false);
  const [editingTeamName, setEditingTeamName] = useState(false);
  const [newTeamName, setNewTeamName] = useState("");
  const [editingTeamLeader, setEditingTeamLeader] = useState(false);
  const [newLeaderSelect, setNewLeaderSelect] = useState<string | null>(null);
  const [kickReason, setKickReason] = useState("");
  // const [isPasswordModalOpen, setIsPasswordModalOpen] = useState(false);
  const [password, setPassword] = useState("");
  const [passwordInput, setPasswordInput] = useState("");
  const navigate = useNavigate();
  const [selectedImage, setSelectedImage] = useState<File | null>(null);
  const [previewImage, setPreviewImage] = useState<string | null>(null);
  const [newSelectedImage, setNewSelectedImage] = useState<File | null>(null);

  const [teamMembers, setTeamMembers] = useState<string[]>([
    "Member 1",
    "Member 2",
    "Member 3",
    "Member 4",
    "Member 5",
    "Member 6",
    "Member 7",
  ]);

  useEffect(() => {
    const fetchTeamInfo = async () => {
      try {
        const response = await axiosInstance.get(`/team/${teamId}`);
        setTeam(response.data);
      } catch (error) {
        console.error("Error fetching team info:", error);
      }
    };

    fetchTeamInfo();
  }, [teamId, accessToken]);

  //팀 수정
  const handleCreateTeam = async () => {
    const formData = new FormData();
    formData.append("teamName", newTeamName);
    formData.append("teamId", team?.teamId.toString() || "");
    if (newSelectedImage instanceof File) {
      formData.append("profileImg", newSelectedImage);
    }
    console.log("FormData:", newSelectedImage);
    const response = await axiosInstance.post("/team/update", formData, {
      withCredentials: true,
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "multipart/form-data",
      },
    });
    console.log("팀 수정 성공 :", response.data);
  };
  //이미지업로드
  const handleFileInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const fileInput = e.target;
    const file = fileInput.files && fileInput.files[0];

    if (file) {
      const reader = new FileReader();
      reader.readAsDataURL(file);

      reader.onload = () => {
        const result = reader.result as string;
        setNewSelectedImage(file); // 파일 경로(string)를 저장
        setPreviewImage(result);
        console.log("Selected Image:", result);
      };

      reader.onerror = (error) => {
        console.error("Error reading the file:", error);
      };
    }
  };

  //팀멤버조회
  const [teamParticipants, setTeamParticipants] = useState<TeamParticipant[]>(
    [],
  );
  useEffect(() => {
    const fetchTeamParticipants = async () => {
      try {
        const response = await axiosInstance.get(
          `/team/${teamId}/participant/list`,
        );
        setTeamParticipants(response.data);
      } catch (error) {
        console.error("Error fetching team participants:", error);
      }
    };

    fetchTeamParticipants();
  }, [teamId, accessToken]);

  //팀멤버
  const filteredTeamMembers = teamParticipants.filter(
    (participant) =>
      participant.teamRole === "MATE" &&
      participant.teamNickName.toLowerCase().includes(searchTeam.toLowerCase()),
  );

  //강퇴
  const handleTeamMemberChange = (
    index: number,
    e: ChangeEvent<HTMLInputElement>,
  ) => {
    const updatedTeamMembers = [...teamMembers];
    updatedTeamMembers[index] = e.target.value;
    setTeamMembers(updatedTeamMembers);
  };

  const handleRemoveMember = (index: number) => {
    setMemberIndexToRemove(index);
    setShowConfirmation(true);
  };

  const cancelRemoveMember = () => {
    setMemberIndexToRemove(null);
    setKickReason("");
    setShowConfirmation(false);
  };

  //강퇴 api 호출
  const handleKickOutMember = async () => {
    if (memberIndexToRemove !== null && kickReason.trim() !== "") {
      const selectedMember = teamParticipants.find(
        (_, index) => index === memberIndexToRemove,
      );

      try {
        const response = await axiosInstance.post("/team/kick-out", {
          teamId: team?.teamId,
          participantId: selectedMember?.teamParticipantsId,
          kickOutReason: kickReason,
        });
        alert("해당 팀원이 강퇴되었습니다.");
        console.log("강퇴 응답:", response.data);
        const updatedTeamParticipants = teamParticipants.filter(
          (_, index) => index !== memberIndexToRemove,
        );
        setTeamParticipants(updatedTeamParticipants);
        setShowConfirmation(false);
      } catch (error) {
        console.error("강퇴 중 오류 발생:", error);
      }
    } else {
      alert("강퇴 사유를 입력하세요.");
    }
  };

  //팀장 권한 부여
  const confirmEditTeamLeader = async () => {
    const selectedParticipant = teamParticipants.find(
      (participant) => participant.teamNickName === newLeaderSelect,
    );

    if (selectedParticipant && selectedParticipant.teamRole === "MATE") {
      try {
        const response = await axiosInstance.patch(
          `/team/${teamId}/participant/${selectedParticipant.teamParticipantsId}`,
          {
            teamRole: "READER",
          },
        );
        alert("팀장이 변경되었습니다.");
        navigate("/homeView");
      } catch (error) {
        console.error("팀장 변경 중 오류:", error);
      }
    } else {
      alert("기존 팀장을 선택했습니다. 다시 선택해 주세요.");
    }

    setEditingTeamLeader(false);
    setNewLeaderSelect(null);
  };

  //팀 삭제
  const [teamNameConfirmationOpen, setTeamNameConfirmationOpen] =
    useState(false);
  const [inputTeamName, setInputTeamName] = useState("");
  const [teamNameError, setTeamNameError] = useState("");

  const openTeamNameConfirmation = () => {
    setInputTeamName("");
    setTeamNameError("");
    setTeamNameConfirmationOpen(true);
  };

  const closeTeamNameConfirmation = () => {
    setTeamNameConfirmationOpen(false);
  };

  const handleTeamNameConfirmation = async () => {
    try {
      if (inputTeamName === team?.name) {
        const response = await axiosInstance.put(`/team/disband`, {
          teamId: team?.teamId,
          teamName: team?.name,
        });
        alert("팀이 삭제되었습니다.");
        console.log("팀 삭제 응답:", response.data);

        // 성공적으로 삭제되었을 때 홈 화면으로 이동하거나 다른 처리를 수행할 수 있습니다.
        navigate("/homeView");
      } else {
        setTeamNameError("팀 명이 올바르지 않습니다.");
        console.error("팀 명이 올바르지 않습니다.");
      }
    } catch (error) {
      console.error("팀 삭제 중 오류:", error);
    }
  };

  //초대코드
  const [codeUrl, setCodeUrl] = useState("");
  useEffect(() => {
    const fetchTeamCode = async () => {
      try {
        const response = await axiosInstance.get(`/team/${teamId}/code`);
        const teamCode = response.data;
        // console.log("초대코드", teamCode);
        const codeUrl = `http://118.67.128.124:8080/team/${teamCode}`;
        setCodeUrl(codeUrl);
        // await axiosInstance.post(codeUrl);
      } catch (error) {
        console.error("Error fetching team code:", error);
      }
    };

    fetchTeamCode();
  }, [teamId, accessToken]);

  const handleCopyClick = async (text: string) => {
    try {
      await navigator.clipboard.writeText(text);
      alert("클립보드에 링크가 복사되었습니다.");
    } catch (e) {
      alert("복사에 실패하였습니다");
    }
  };

  return (
    <TeamLeaderContainer>
      <div>
        <h3
          style={{ textAlign: "center", fontSize: "30px", fontWeight: "bold" }}
        >
          팀 프로필
        </h3>
        <TeamInfoContainer>
          <TeamImageContainer>
            <label>
              <img
                src={
                  typeof selectedImage === "string"
                    ? selectedImage
                    : previewImage || team?.profileUrl || profileImg
                }
                alt="Team Logo"
                // onClick={() => document.getElementById("imageUpload")?.click()}
                style={{
                  width: "200px",
                  height: "200px",
                  cursor: "pointer",
                  marginTop: "20px",
                  marginLeft: "35px",
                  marginBottom: "20px",
                }}
              />
              <span>팀명 </span>
              <input
                title="imgUpload"
                id="imageUpload"
                type="file"
                accept="image/*"
                onChange={handleFileInputChange}
                style={{ display: "none", width: "100%" }}
              />
            </label>
            <span title="teamNameChange">
              <input
                style={{
                  width: "150px",
                  height: "40px",
                  marginRight: "10px",
                  padding: "5px",
                }}
                id="teamName"
                placeholder=" 팀 이름"
                title="text"
                type="text"
                value={newTeamName || team?.name || ""}
                onChange={(e) => setNewTeamName(e.target.value)}
              />
            </span>
            <StyledButton onClick={handleCreateTeam}>변경하기</StyledButton>
            <br />
          </TeamImageContainer>
          <div>
            <span>팀장 </span>
            <select
              style={{ width: "150px", height: "40px", marginRight: "10px" }}
              title="select"
              value={newLeaderSelect || ""}
              onChange={(e) => setNewLeaderSelect(e.target.value)}
            >
              <option
                value={
                  teamParticipants.find(
                    (participant) => participant.teamRole === "READER",
                  )?.teamNickName
                }
              >
                {
                  teamParticipants.find(
                    (participant) => participant.teamRole === "READER",
                  )?.teamNickName
                }
              </option>
              {teamParticipants
                .filter((participant) => participant.teamRole !== "READER")
                .map((participant, index) => (
                  <option key={index} value={participant.teamNickName}>
                    {participant.teamNickName}
                  </option>
                ))}
            </select>
            <StyledButton onClick={confirmEditTeamLeader}>
              변경하기
            </StyledButton>
          </div>
        </TeamInfoContainer>
      </div>
      <br />
      <div>
        <span style={{ display: "flex", alignItems: "center" }}>
          <span>초대코드 URL</span>
          <img
            style={{
              width: "30px",
              cursor: "pointer",
            }}
            src={linkImg}
            alt="복사"
            onClick={() => handleCopyClick(codeUrl)}
          />
        </span>
      </div>
      <TeamMembersContainer>
        <StyledInput
          style={{
            width: "300px",
          }}
          type="text"
          placeholder="팀원을 검색하세요"
          value={searchTeam}
          onChange={(e) => setSearchTeam(e.target.value)}
        />
        <div
          style={{
            marginTop: "10px",
            maxHeight: "250px",
            overflowY: "auto",
          }}
        >
          {filteredTeamMembers.map((participant, index) => (
            <div
              key={index}
              style={{
                display: "flex",
                marginBottom: "10px",
              }}
            >
              <StyledInput
                title="disband"
                type="text"
                value={participant.teamNickName}
                onChange={(e) => handleTeamMemberChange(index, e)}
              />
              <StyledButton onClick={() => handleRemoveMember(index)}>
                강퇴
              </StyledButton>
            </div>
          ))}
        </div>
      </TeamMembersContainer>

      {showConfirmation && (
        <ConfirmationModal>
          <p>강퇴하시겠습니까?</p>
          {memberIndexToRemove !== null && (
            <>
              <p>강퇴 사유를 입력하세요:</p>
              <StyledInput
                title="reason"
                type="text"
                value={kickReason}
                onChange={(e) => setKickReason(e.target.value)}
              />
            </>
          )}
          <StyledButton onClick={handleKickOutMember}>확인</StyledButton>
          <StyledButton onClick={cancelRemoveMember}>취소</StyledButton>
        </ConfirmationModal>
      )}

      <div style={{ textAlign: "center", marginTop: "20px" }}>
        <StyledButton onClick={openTeamNameConfirmation}>
          팀 계정 삭제하기
        </StyledButton>
      </div>
      <StyledModal
        isOpen={teamNameConfirmationOpen}
        onClose={closeTeamNameConfirmation}
      >
        <div className="modal-content">
          <div>팀 계정을 삭제하시겠습니까?</div>
          <p>팀 명을 입력하세요:</p>
          <input
            title="teamName"
            type="text"
            value={inputTeamName}
            onChange={(e) => setInputTeamName(e.target.value)}
          />
          {teamNameError && <p style={{ color: "red" }}>{teamNameError}</p>}
          <div>
            <StyledButton onClick={handleTeamNameConfirmation}>
              확인
            </StyledButton>
            &nbsp;
            <StyledButton onClick={closeTeamNameConfirmation}>
              취소
            </StyledButton>
          </div>
        </div>
      </StyledModal>
    </TeamLeaderContainer>
  );
}
