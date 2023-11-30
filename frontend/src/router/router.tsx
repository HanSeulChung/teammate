import { Routes, Route } from 'react-router-dom';
import TeamCalender from '../components/TeamCalender';

const Router = () => {
    return (
        <Routes>
			{/* <Route path='*' element={<Error />} /> */}
			<Route path='/' element={<TeamCalender />} />
			{/* <Route /> */}
		</Routes>
    );
};

export default Router;