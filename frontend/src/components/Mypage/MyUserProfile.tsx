import React ,{useState}from "react";
import { User, Team } from "../../state/authState"; // User 타입을 import
import {UserProfileContainer,UserProfileTitle,UserProfileInfo,Email,UpdateButton} from "./MyUserProfileStyled";
import PasswordChangeModal from "./PasswordChangeModal";

interface UserProfileProps {
  user: User | null;
  teamList: Team[];
  selectedTeam: string | null;
  handleTeamSelect: (event: React.ChangeEvent<HTMLSelectElement>) => void;
}

const UserProfile: React.FC<UserProfileProps> = ({
  user,
  teamList,
  selectedTeam,
  handleTeamSelect,
}) => {
  const [isPasswordChangeModalOpen, setIsPasswordChangeModalOpen] = useState(false);

  const handleOpenPasswordChangeModal = () => {
    setIsPasswordChangeModalOpen(true);
  };

  const handleClosePasswordChangeModal = () => {
    setIsPasswordChangeModalOpen(false);
  };
  return (
    <UserProfileContainer>
      <UserProfileTitle>내 프로필</UserProfileTitle>
      <br />
      {user && (
        <UserProfileInfo>
          <p>이름: {user.name}</p>
          <span>
            <Email>Email: {user.id}</Email>
            <UpdateButton onClick={handleOpenPasswordChangeModal}>
            비밀번호 변경
          </UpdateButton>
          </span>
          <select
            title="myteam"
            id="teamSelect"
            value={selectedTeam || ""}
            onChange={handleTeamSelect}
          >
            <option value="" disabled>
              팀을 선택하세요
            </option>
            {teamList.map((team) => (
              <option key={team.name} value={team.name}>
                {team.name}
              </option>
            ))}
          </select>
        </UserProfileInfo>
      )}
   {isPasswordChangeModalOpen && (
        <PasswordChangeModal onClose={handleClosePasswordChangeModal} />
      )}
    </UserProfileContainer>
  );
};

export default UserProfile;
