import mainImage from '../assets/main_image.png'
import {MainContentsSpan} from '../styles/MainContentsStyled'

const MainContents = () => {
    return (
        <MainContentsSpan>
            <div>
                <h2>팀원들과의 원활한 협업을 위한 TeamMate, 지금 시작해보세요.</h2>
                <p>
                    글을 작성하고 계획을 세우며, 다른 이들과 협업해보세요.
                </p>
            </div>
            <img src={mainImage} />
        </MainContentsSpan>
    );
};

export default MainContents;