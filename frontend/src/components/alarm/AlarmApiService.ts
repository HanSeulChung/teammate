const BASE_URL = "http://118.67.128.124:8080";
export const fetchPersonalAlarms = async (accessToken: string): Promise<any> => {
  const url = `${BASE_URL}/notifications/period?days=7&page=0&size=10`;

  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${accessToken}`,
      },
    });

    if (!response.ok) {
      throw new Error('Failed to fetch personal alarms');
    }

    return response.json();
  } catch (error) {
    console.error('Error fetching personal alarms:', error);
    throw error;
  }
};
