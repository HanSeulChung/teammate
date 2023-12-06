import { Routes, Route } from "react-router-dom";

import Calender from "../views/Calender";
import SignInView from "../views/SignInView";
import SignUp from "../components/Join/SignUp";
import Index from "../views/Index";
import KakaoLogin from "../components/Login/KakaoLogin";
import Home from "../components/Home/HomeContent";
import Profile from "../components/Profile/Profile";
import HomeView from "../views/HomeView";

const Router = () => {
  return (
    <Routes>
      <Route path="/" element={<Index />} />
      <Route path="/캘린더" element={<Calender />} />
      <Route path="/signup" element={<SignUp />} />
      <Route path="/signin" element={<SignInView />} />
      <Route path="/kakaoLogin" element={<KakaoLogin />} />
      <Route path="/home" element={<Home />} />
      <Route path="/homeview" element={<HomeView />} />
      <Route path="/profile" element={<Profile />} />
      <Route />
    </Routes>
  );
};

export default Router;
