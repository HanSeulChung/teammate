import { Routes, Route } from 'react-router-dom';

import Calender from '../views/Calender';
import SignInView from '../views/SignInView';
import SignUp from '../components/SignUp';
import Index from '../views/Index';
import KakaoLogin from '../components/KakaoLogin';
// import Redirection from '../components/Redirection';

const Router = () => {
    return (
        <Routes>
			{/* <Route path='*' element={<Error />} /> */}
			<Route path='/' element={<Index />} />
			<Route path='/캘린더' element={<Calender />} />
			<Route path='/signup' element={<SignUp />} />
			<Route path='/signin' element={<SignInView />} />
			<Route path='/kakaoLogin' element={<KakaoLogin />} />
			{/* <Route path='/authgoogle' element={<GoogleRedirect />} />
			<Route path='/authnaver' element={<NaverRedirect />} /> */}
			<Route />
		</Routes>
    );
};

export default Router;
