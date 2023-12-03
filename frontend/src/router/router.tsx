import { Routes, Route } from 'react-router-dom';
import Calender from '../views/Calender';
import SignIn from '../components/SignIn';
import SignUp from '../components/SignUp';

const Router = () => {
    return (
        <Routes>
			<Route path='/로그인' element={<SignIn />} />
			<Route path='/회원가입' element={<SignUp />} />
			<Route path='/캘린더' element={<Calender />} />
			<Route />
		</Routes>
    );
};

export default Router;