import { Link } from 'react-router-dom';
import mainImage from '../assets/main_image.png'
import { MainContentsSpan } from '../styles/MainContentsStyled'

const MainContents = () => {
    return (
        <MainContentsSpan>
            <div>
                <h2 className="mb-4 text-4xl font-extrabold leading-none tracking-tight text-gray-900 md:text-5xl lg:text-6xl">
                    팀원들과의 원활한 협업을 위한 <span className="text-green-600 dark:text-green-500">TeamMate,</span> 지금 시작해보세요.
                </h2>
                <p className="mb-4 text-lg font-normal text-gray-500 lg:text-xl">
                    글을 작성하고 계획을 세우며, 다른 이들과 협업해보세요.
                </p>
                <Link to="/signUp" className="inline-flex items-center justify-center px-5 py-3 text-base font-medium text-center text-white bg-green-700 rounded-lg hover:bg-green-800 focus:ring-4 focus:ring-blue-300">
                    시작하기
                </Link>
            </div>
            <img src={mainImage} />
        </MainContentsSpan>
    );
};

export default MainContents;