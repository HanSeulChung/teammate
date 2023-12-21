import { useEffect } from "react";
import HomeContent from "../components/Home/HomeContent";
import HomeCreateTeamBtn from "../components/Home/HomeCreateTeamBtn";
import HomeSearchBar from "../components/Home/HomeSearchBar";

const homeview = () => {
  useEffect(() => {
    const preventGoBack = () => {
      history.go(1);
      console.log("prevent go back!");
    };

    history.pushState(null, "", location.href);
    window.addEventListener("popstate", preventGoBack);

    return () => window.removeEventListener("popstate", preventGoBack);
  }, []);

  return (
    <div>
      <HomeSearchBar />
      <HomeCreateTeamBtn />
      <HomeContent />
    </div>
  );
};

export default homeview;
