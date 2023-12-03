import { Routes, Route } from 'react-router-dom';

import Calender from '../views/Calender';
import SignIn from '../components/SignIn';
import SignUp from '../components/SignUp';

const Router = () => {
    return (
        <Routes>
			{/* <Route path='*' element={<Error />} /> */}
			<Route path='/캘린더' element={<Calender />} />
			<Route />
		</Routes>
    );
};

export default Router;