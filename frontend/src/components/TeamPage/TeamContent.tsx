import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import axiosInstance from "../../axios";
import documentImg from "../../assets/document-icon.png";
import calendarImg from "../../assets/calendar-icon.png";

const TeamContent = () => {
  // 팀 아이디
  const { teamId } = useParams();
  
  const [teamName, setTeamName] = useState("");

  useEffect(() => {
    const fetchTeamData = async () => {
      try {
        const response = await axiosInstance.get(`/team/${teamId}`, {});
        setTeamName(response.data.name);
      } catch (error) {
        console.error("Error fetching team data:", error);
      }
    };

    fetchTeamData();
  }, [teamId]);
  // 팀 문서, 캘린더 경로
  const documentRoute = `/team/${teamId}/documentsList`;
  const calenderRoute = `/team/${teamId}/schedule`;

  return (
    <div className="py-8 px-4 mx-auto max-w-screen-xl lg:py-16 bg-white">
      <div className="bg-gray-50 border-gray-200 rounded-lg p-8 md:p-12 mb-8">
        <h2 className="text-gray-900 text-3xl md:text-5xl font-extrabold mb-2">
          {teamName}팀의 홈
        </h2>
        <p className="text-lg font-normal text-gray-500 mb-6">
          이용하실 컨텐츠를 클릭하세요. 해당 페이지로 이동됩니다.
        </p>
      </div>
      <div className="grid md:grid-cols-2 gap-8">
        <Link
          to={documentRoute}
          className="block hover:bg-gray-100 bg-gray-50 border-gray-200 rounded-lg p-8 md:p-12"
        >
          <img src={documentImg} className="w-10" />
          <h3 className="mb-2 text-3xl font-bold tracking-tight text-gray-90">
            문서
          </h3>
          <p className="font-normal text-gray-700">
            실시간으로 문서를 작성하고 공유
          </p>
        </Link>
        <Link
          to={calenderRoute}
          className="block hover:bg-gray-100 bg-gray-50 border-gray-200 rounded-lg p-8 md:p-12"
        >
          <img src={calendarImg} className="w-10" />
          <h3 className="mb-2 text-3xl font-bold tracking-tight text-gray-90">
            캘린더
          </h3>
          <p className="font-normal text-gray-700">
            팀 일정을 공유할 수 있는 캘린더
          </p>
        </Link>
      </div>
    </div>
  );
};

export default TeamContent;
