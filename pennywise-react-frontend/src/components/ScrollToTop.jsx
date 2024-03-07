import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

const ScrollToTop = () => {
  const { pathname } = useLocation();

  useEffect(() => {
    window.scrollTo(0, 0); // Scrolls to the top when the pathname changes
  }, [pathname]);

  return null; // Renders nothing, just handles the scroll effect
};

export default ScrollToTop;