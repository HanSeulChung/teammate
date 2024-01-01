import { useEffect, useState } from "react";
import styled from "styled-components";
import HomeContent from "../components/Home/HomeContent";
import HomeCreateTeamBtn from "../components/Home/HomeCreateTeamBtn";
import HomeSearchBar from "../components/Home/HomeSearchBar";

const homeView = () => {
  const [, setSearch] = useState("");
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
    <HomeViewContainer>
      <HomeSearchBar onSearch={handleSearch} />
      <HomeCreateTeamBtn />
      <HomeContent />
    </HomeViewContainer>
  );
};

export default homeView;

const HomeViewContainer = styled.div`
  max-width: 800px;
  margin: 0 auto;
`;
