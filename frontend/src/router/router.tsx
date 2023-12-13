import { Routes, Route, Navigate } from "react-router-dom";

import Calender from "../views/Calender";

import TextEditorView from "../views/TextEditorView";
import SignInView from "../views/SignInView";
import SignUp from "../components/Join/SignUp";
import Index from "../views/Index";
import KakaoLogin from "../components/Login/KakaoLogin";
import Home from "../components/Home/HomeContent";
import HomeView from "../views/HomeView";
import { v4 as uuidV4 } from "uuid";
import React from "react";
import CreateTextView from "../views/CreateTextView";
import DocumentListView from "../views/DocumentListView";
import CommentView from "../views/CommentView";
import TeamDetail from "../components/TeamPage/TeamDetail";
import TeamLeader from "../components/ProfilePage/TeamLeader";
import TeamMembers from "../components/ProfilePage/TeamMembers";
import TeamCreateView from "../views/TeamCreateView";
import Mypage from "../components/Mypage/Mypage";

const Router = () => {
  return (
    <Routes>
      <Route path="/캘린더" element={<Calender />} />
      <Route path="/team/:teamId/document" element={<CreateTextView />} />
      <Route
        path="/team/:teamId/documents/:documentsId"
        element={<TextEditorView />}
      />
      <Route
        path="/team/:teamId/documents/:documentsId/comment"
        element={<CommentView />}
      />
      <Route path="/text-list/" element={<DocumentListView />} />
      <Route path="/" element={<Index />} />
      <Route path="/schedules" element={<Calender />} />
      <Route path="/signup" element={<SignUp />} />
      <Route path="/signin" element={<SignInView />} />
      <Route path="/kakaoLogin" element={<KakaoLogin />} />
      <Route path="/home" element={<Home />} />
      <Route path="/homeview" element={<HomeView />} />
      <Route path="/team/:teamId" element={<TeamDetail />} />
      <Route path="/teamleader" element={<TeamLeader />} />
      <Route path="/teammembers" element={<TeamMembers />} />
      <Route path="/teamcreateview" element={<TeamCreateView />} />
      <Route path="/mypage" element={<Mypage />} />
      <Route />
    </Routes>
  );
};

export default Router;
