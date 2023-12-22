import { useEffect } from "react";
import { Routes, Route, useNavigate } from "react-router-dom";

import Index from "../views/Index";
import SignUp from "../components/Join/SignUp";
import SignInView from "../views/SignInView";
import KakaoLogin from "../components/Login/KakaoLogin";
import HomeView from "../views/HomeView";

import Calender from "../views/Calender";
import CreateTextView from "../views/CreateTextView";
import TextEditorView from "../views/TextEditorView";
import DocumentListView from "../views/DocumentListView";
import CommentView from "../views/CommentView";

import MyPage from "../components/MyInfoPage/MyPage";
import TeamInfo from "../components/TeamCreate/TeamInfo";
import TeamLeader from "../components/ProfilePage/TeamLeader";
import TeamMembers from "../components/ProfilePage/TeamMembers";
// import TeamDetail from "../components/TeamPage/TeamDetail";
// import { v4 as uuidV4 } from "uuid";

const Router = () => {
  const navigate = useNavigate();

  useEffect(() => {
    if (localStorage.getItem("accessToken")) return;
    else navigate("/");
  }, []);

  return (
    <Routes>
      <Route path="/" element={<Index />} />
      <Route path="/signUp" element={<SignUp />} />
      <Route path="/signIn" element={<SignInView />} />
      <Route path="/kakaoLogin" element={<KakaoLogin />} />
      <Route path="/homeView" element={<HomeView />} />
      <Route path="/team/:teamId/schedules" element={<Calender />} />
      <Route path="/team/:teamId/documents" element={<CreateTextView />} />
      <Route path="/team/:teamId/documents/:documentsId" element={<TextEditorView />} />
      <Route path="/team/:teamId/documentsList/" element={<DocumentListView />} />
      <Route path="/team/:teamId/documents/:documentsId/comment" element={<CommentView />} />
      <Route path="/myPage" element={<MyPage />} />
      <Route path="/TeamInfo" element={<TeamInfo />} />
      <Route path="/team/:teamId/teamLeader" element={<TeamLeader />} />
      <Route path="/team/:teamId/teamMembers" element={<TeamMembers />} />
      {/* <Route path="/team/:teamId" element={<TeamDetail />} /> */}
    </Routes>
  );
};

export default Router;
