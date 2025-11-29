import { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import VideoPlayer from "./VideoPlayer";

export default function SeriesPage() {
  const { id } = useParams(); // <-- GET seriesId from URL!
  const [series, setSeries] = useState(null);
  const [currentVideo, setCurrentVideo] = useState("");
  const [activeContentId, setActiveContentId] = useState(null);

  useEffect(() => {
  const fetchSeries = async () => {
    try {
      const token = localStorage.getItem("accessToken");

      if (!token) {
        console.error("No token found in localStorage");
        return;
      }

      const res = await axios.get(
        `http://192.168.0.109:8080/api/v1/user-series/getById/${id}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setSeries(res.data);

      if (res.data.content && res.data.content.length > 0) {
        const firstContentId = res.data.content[0].contentId;

        setCurrentVideo(
          `http://192.168.0.109:8080/api/v1/stream/${firstContentId}/master.m3u8`
        );

        setActiveContentId(firstContentId);
      }
    } catch (err) {
      console.error("Error loading series:", err);
    }
  };

  fetchSeries();
}, [id]);

  const playEpisode = (contentId) => {
    setCurrentVideo(
      `http://192.168.0.109:8080/api/v1/stream/${contentId}/master.m3u8`
    );
    setActiveContentId(contentId);
  };

  if (!series) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-950">
        <p className="text-gray-300 text-lg">Loading series...</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-950 text-gray-100">
      <div className="max-w-5xl mx-auto px-4 py-6 md:py-10">

        {/* Title */}
        <h1 className="text-2xl md:text-3xl font-semibold mb-2">
          {series.title}
        </h1>
        <p className="text-gray-300 mb-4">{series.description}</p>

        {/* Video Player */}
        <div className="bg-gray-900 rounded-xl p-4 mb-6 border border-gray-800">
          <div className="w-full aspect-video bg-black rounded-lg overflow-hidden">
            <VideoPlayer src={currentVideo} />
          </div>
        </div>

        {/* Episode Buttons */}
        <h2 className="text-xl font-semibold mb-3">Episodes</h2>

        <div className="flex flex-wrap gap-3">
          {series.content.map((ep, index) => (
            <button
              key={ep.contentId}
              onClick={() => playEpisode(ep.contentId)}
              className={`
                px-4 py-2 rounded-full text-sm font-medium border transition
                ${
                  activeContentId === ep.contentId
                    ? "bg-emerald-500 text-black border-emerald-400 shadow-md"
                    : "bg-gray-900 text-gray-200 border-gray-700 hover:bg-gray-800"
                }
              `}
            >
              EP {index + 1}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}
