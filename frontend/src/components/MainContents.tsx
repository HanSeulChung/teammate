import mainImage from '../assets/main_image.png'
import { MainContentsSpan } from '../styles/MainContentsStyled'

const MainContents = () => {
    return (
        <MainContentsSpan>
            <div>
                {/* <h2>팀원들과의 원활한 협업을 위한 TeamMate, 지금 시작해보세요.</h2>
                <p>
                    글을 작성하고 계획을 세우며, 다른 이들과 협업해보세요.
                </p> */}

                <h1 className="mb-4 text-4xl font-extrabold leading-none tracking-tight text-gray-900 md:text-5xl lg:text-6xl dark:text-white">
                    팀원들과의 원활한 협업을 위한 <span className="text-green-600 dark:text-green-500">TeamMate,</span> 지금 시작해보세요.</h1>
                <p className="text-lg font-normal text-gray-500 lg:text-xl dark:text-gray-400">
                    글을 작성하고 계획을 세우며, 다른 이들과 협업해보세요.
                </p>
                <button className="inline-flex items-center justify-center px-5 py-3 text-base font-medium text-center text-white bg-green-700 rounded-lg hover:bg-green-800 focus:ring-4 focus:ring-blue-300 dark:focus:ring-green-900">
                    시작하기
                </button>
            </div>

            <img src={mainImage} />
        </MainContentsSpan>
    );
};

export default MainContents;