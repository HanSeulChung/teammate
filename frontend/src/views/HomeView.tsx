import { useEffect, useState } from "react";
import HomeContent from "../components/Home/HomeContent";
import HomeCreateTeamBtn from "../components/Home/HomeCreateTeamBtn";
import HomeSearchBar from "../components/Home/HomeSearchBar";

const homeView = () => {
  const [search, setSearch] = useState("");
  const handleSearch = (value: any) => {
    setSearch(value);
  };

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
      <HomeSearchBar onSearch={handleSearch} />
      <HomeCreateTeamBtn />
      <HomeContent />
    </div>
  );
};

export default homeView;
