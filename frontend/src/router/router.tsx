import { Routes, Route } from 'react-router-dom';

import Calender from '../views/Calender';

const Router = () => {
    return (
        <Routes>
			{/* <Route path='*' element={<Error />} /> */}

			<Route path='/캘린더' element={<Calender />} />
			{/* <Route /> */}
		</Routes>
    );
};

export default Router;