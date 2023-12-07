import HomeContent from "../components/Home/HomeContent";
import HomeCreateTeamBtn from "../components/Home/HomeCreateTeamBtn";
import HomeSearchBar from "../components/Home/HomeSearchBar";

const homeview = () => {
  return (
    <div>
      <HomeSearchBar />
      <HomeCreateTeamBtn />
      <HomeContent />
    </div>
  );
};

export default homeview;
