import logo from '../../assets/logo.png'
import { Link } from 'react-router-dom';
import { HeaderImg, HeaderUl, HeaderTag, HeaderLink } from '../../styles/HeaderStyled'

const Header = () => {
    return (
        <HeaderTag>
            <Link to='/'>
                <HeaderImg src={logo} />
            </Link>
            <nav>
                <HeaderUl>
                    <li><Link to='/'>로그인</Link></li>
                    <li>
                        <HeaderLink to='/'>시작하기</HeaderLink>
                    </li>
                </HeaderUl>
            </nav>
        </HeaderTag>
    );
};

export default Header;