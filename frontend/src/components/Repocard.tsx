import "./RepoCard.scss";

interface RepoCardProps {
  name: string;
  htmlUrl: string;
  isPrivate: boolean;
}

export default function RepoCard({ name, htmlUrl, isPrivate }: RepoCardProps) {
  return (
    <a className="repo-card" href={htmlUrl} target="_blank" rel="noreferrer">
      <div className="repo-header">
        <span className="repo-name">{name}</span>
        <span className={`repo-badge ${isPrivate ? "private" : "public"}`}>
          {isPrivate ? "Private" : "Public"}
        </span>
      </div>

      <div className="repo-footer">
        <span>View on GitHub →</span>
      </div>
    </a>
  );
}